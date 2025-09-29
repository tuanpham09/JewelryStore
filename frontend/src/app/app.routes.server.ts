import { RenderMode, ServerRoute } from '@angular/ssr';

export const serverRoutes: ServerRoute[] = [
  // Dynamic route - do NOT prerender. Use server rendering at runtime instead.
  {
    path: 'product/:id',
    renderMode: RenderMode.Server
  },
  // Fallback: prerender all other static routes
  {
    path: '**',
    renderMode: RenderMode.Prerender
  }
];
