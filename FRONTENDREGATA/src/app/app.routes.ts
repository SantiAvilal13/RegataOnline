import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'home', loadComponent: () => import('./components/home/home/home.component').then(m => m.HomeComponent) },
  
  // Rutas de Usuarios
  { path: 'usuarios', loadComponent: () => import('./components/usuarios/usuarios-list/usuarios-list.component').then(m => m.UsuariosListComponent) },
  { path: 'usuarios/new', loadComponent: () => import('./components/usuarios/usuario-form/usuario-form.component').then(m => m.UsuarioFormComponent) },
  { path: 'usuarios/:id', loadComponent: () => import('./components/usuarios/usuario-detail/usuario-detail.component').then(m => m.UsuarioDetailComponent) },
  { path: 'usuarios/:id/edit', loadComponent: () => import('./components/usuarios/usuario-form/usuario-form.component').then(m => m.UsuarioFormComponent) },
  
  // Rutas de Barcos
  { path: 'barcos', loadComponent: () => import('./components/barcos/barcos-list/barcos-list.component').then(m => m.BarcosListComponent) },
  { path: 'barcos/new', loadComponent: () => import('./components/barcos/barco-form/barco-form.component').then(m => m.BarcoFormComponent) },
  { path: 'barcos/:id', loadComponent: () => import('./components/barcos/barco-detail/barco-detail.component').then(m => m.BarcoDetailComponent) },
  { path: 'barcos/:id/edit', loadComponent: () => import('./components/barcos/barco-form/barco-form.component').then(m => m.BarcoFormComponent) },
  
  // Rutas de Modelos
  { path: 'modelos', loadComponent: () => import('./components/modelos/modelos-list/modelos-list.component').then(m => m.ModelosListComponent) },
  { path: 'modelos/new', loadComponent: () => import('./components/modelos/modelo-form/modelo-form.component').then(m => m.ModeloFormComponent) },
  { path: 'modelos/:id', loadComponent: () => import('./components/modelos/modelo-detail/modelo-detail.component').then(m => m.ModeloDetailComponent) },
  { path: 'modelos/:id/edit', loadComponent: () => import('./components/modelos/modelo-form/modelo-form.component').then(m => m.ModeloFormComponent) },
  
  // Rutas de Partidas
  { path: 'partidas', loadComponent: () => import('./components/partidas/partidas-list/partidas-list.component').then(m => m.PartidasListComponent) },
  { path: 'partidas/new', loadComponent: () => import('./components/partidas/partida-form/partida-form.component').then(m => m.PartidaFormComponent) },
  { path: 'partidas/:id', loadComponent: () => import('./components/partidas/partida-detail/partida-detail.component').then(m => m.PartidaDetailComponent) },
  
  // Ruta de Juego
  { path: 'game', loadComponent: () => import('./components/game/game-board/game-board.component').then(m => m.GameBoardComponent) },
  
  // Ruta 404
  { path: '**', redirectTo: '/home' }
];
