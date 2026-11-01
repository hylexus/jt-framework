/*
 * Portions correspond to element-plus-vite-starter.
 * Comparison snapshot: https://github.com/element-plus/element-plus-vite-starter/blob/fa10f32788a09b2faab73d3f9709f33bc7cf4d3f/
 * Local modifications by hylexus; source attribution restored 2026-10-02.
 * Upstream package metadata declares MIT; complete original notice remains
 * unresolved. See licenses/element-plus-vite-starter-NOTICE.txt in the repository.
 */

import { useDark, useToggle } from '@vueuse/core'

export const isDark = useDark()
export const toggleDark = useToggle(isDark)
