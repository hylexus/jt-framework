/*
 * Portions correspond to element-plus-vite-starter.
 * Comparison snapshot: https://github.com/element-plus/element-plus-vite-starter/blob/fa10f32788a09b2faab73d3f9709f33bc7cf4d3f/
 * Local modifications by hylexus; source attribution restored 2026-10-02.
 * Upstream package metadata declares MIT.
 * See public/NOTICE.txt in this UI module for source and license details.
 */

import { useDark, useToggle } from '@vueuse/core'

export const isDark = useDark()
export const toggleDark = useToggle(isDark)
