import { createHash } from 'node:crypto';
import { existsSync, mkdirSync, readFileSync, realpathSync, writeFileSync } from 'node:fs';
import { dirname, isAbsolute, relative, resolve } from 'node:path';
import { createRequire } from 'node:module';
import { fileURLToPath } from 'node:url';

const root = fileURLToPath(new URL('../', import.meta.url));
const inventoryPath = resolve(root, 'licenses/docs-frontend-inventory.json');
const noticesPath = resolve(root, 'docs/src/.vuepress/public/THIRD-PARTY-NOTICES.txt');
const sha256 = (bytes) => createHash('sha256').update(bytes).digest('hex');

function packageForFile(path) {
  let directory = dirname(realpathSync(path));
  while (directory.replaceAll('\\', '/').includes('/node_modules/')) {
    const metadataPath = resolve(directory, 'package.json');
    if (existsSync(metadataPath)) {
      const metadata = JSON.parse(readFileSync(metadataPath, 'utf8'));
      if (metadata.name && metadata.version) return { name: metadata.name,
        version: metadata.version, packageRoot: relative(root, directory), modules: [] };
    }
    directory = dirname(directory);
  }
}

export function docsSystemFonts() {
  return {
    postcssPlugin: 'jt-docs-system-fonts',
    OnceExit(styles) {
      styles.walkAtRules(/^font-face$/i, (rule) => {
        const family = rule.nodes?.find((node) => node.type === 'decl' && node.prop.toLowerCase() === 'font-family')
          ?.value.replace(/["']/g, '').trim().toLowerCase();
        // The pinned Hope theme embeds a Crimson subset whose font license is
        // unconfirmed. Use system fonts and require review of any new font face.
        if (family !== 'crimson') {
          throw rule.error('Docs use system fonts; review this new @font-face before distributing it.');
        }
        rule.remove();
      });
    },
  };
}

export function docsLicenseMaterials() {
  return {
    name: 'jt-docs-license-materials',
    // The server renderer is a build tool, not part of the browser distribution.
    apply: (config, environment) => environment.command === 'build' && !config.build?.ssr,
    generateBundle(_options, bundle) {
      const included = new Set(Object.values(bundle).filter((entry) => entry.type === 'chunk')
        .flatMap((entry) => Object.keys(entry.modules)));
      for (const id of this.getModuleIds()) {
        // CSS modules can have no rendered JS while still contributing styles.
        if (this.getModuleInfo(id)?.isIncluded) included.add(id);
      }
      const packages = new Map();
      for (const id of included) {
        const path = id.split('?')[0];
        if (!isAbsolute(path) || !path.replaceAll('\\', '/').includes('/node_modules/') || !existsSync(path)) continue;
        const metadata = packageForFile(path);
        if (!metadata) continue;
        const key = `${metadata.name}@${metadata.version}`;
        const record = packages.get(key) ?? metadata;
        record.modules.push(relative(root, path));
        packages.set(key, record);
      }
      const inventoryBytes = readFileSync(inventoryPath);
      const inventory = JSON.parse(inventoryBytes.toString('utf8'));
      const localAssets = inventory.localAssets ?? [];
      for (const asset of localAssets) {
        for (const record of [...asset.files, ...asset.licenseFiles]) {
          if (sha256(readFileSync(resolve(root, record.path))) !== record.sha256) {
            this.error(`Local browser asset or license changed; review ${record.path}`);
          }
        }
      }
      // Search Pro copies an already bundled worker after Vite finishes. Its
      // published source map identifies two additional embedded source files.
      const prebuiltAssets = [];
      for (const asset of inventory.prebuiltAssets ?? []) {
        const provider = packages.get(`${asset.package}@${asset.version}`);
        if (!provider) continue;
        const directory = resolve(root, provider.packageRoot);
        const source = readFileSync(resolve(directory, asset.entry));
        const mapBytes = readFileSync(resolve(directory, asset.sourceMap));
        if (sha256(source) !== asset.sha256 || sha256(mapBytes) !== asset.sourceMapSha256) {
          this.error(`Prebuilt browser asset changed: ${asset.package}/${asset.entry}`);
        }
        const map = JSON.parse(mapBytes.toString('utf8'));
        const require = createRequire(resolve(directory, 'package.json'));
        for (const dependency of asset.dependencies) {
          const metadata = packageForFile(require.resolve(dependency.name));
          const content = map.sourcesContent[dependency.sourceMapIndex];
          if (metadata?.name !== dependency.name || metadata.version !== dependency.version || typeof content !== 'string' ||
              sha256(content) !== dependency.sha256 ||
              sha256(readFileSync(resolve(root, metadata.packageRoot, dependency.source))) !== dependency.sha256) {
            this.error(`Prebuilt asset source differs from reviewed package: ${dependency.name}`);
          }
          const key = `${metadata.name}@${metadata.version}`;
          const record = packages.get(key) ?? metadata;
          record.modules.push(`${provider.packageRoot}/${asset.entry} (source map: ${dependency.source})`);
          packages.set(key, record);
        }
        prebuiltAssets.push({ package: asset.package, version: asset.version,
          entry: asset.entry, sha256: sha256(source), sourceMapSha256: sha256(mapBytes) });
      }
      const records = [...packages.values()].sort((a, b) => `${a.name}@${a.version}`.localeCompare(`${b.name}@${b.version}`));
      const evidenceDirectory = resolve(root, 'build/docs-license-materials');
      mkdirSync(evidenceDirectory, { recursive: true });
      writeFileSync(resolve(evidenceDirectory, 'browser-packages.json'), JSON.stringify({ packages: records }, null, 2) + '\n');
      if (process.env.JT_DOCS_LICENSE_DISCOVER === '1') return;
      const lock = readFileSync(resolve(root, inventory.lockfile));
      if (sha256(lock) !== inventory.lockfileSha256) this.error('Docs lockfile changed; review and regenerate its license inventory.');
      const reviewed = new Set(inventory.packages.map((item) => `${item.name}@${item.version}`));
      const missing = records.filter((item) => !reviewed.has(`${item.name}@${item.version}`));
      if (missing.length) this.error(`Unreviewed browser packages: ${missing.map((item) => `${item.name}@${item.version}`).join(', ')}`);
      const notices = readFileSync(noticesPath);
      if (sha256(notices) !== inventory.noticesSha256) this.error('Docs third-party notices changed; regenerate the reviewed materials.');
      this.emitFile({ type: 'asset', fileName: 'license-build-provenance.json', source: JSON.stringify({
        lockfile: inventory.lockfile, lockfileSha256: sha256(lock), inventorySha256: sha256(inventoryBytes),
        thirdPartyNoticesSha256: sha256(notices), packages: records.map(({ name, version }) => ({ name, version })),
        prebuiltAssets, localAssets,
      }, null, 2) + '\n' });
    },
  };
}
