import { Routes } from '@angular/router';
import { LoginComponent } from './components/security/login/login.component';
import { RegisterComponent } from './components/security/register/register.component';
import { HomeComponent } from './components/home/home/home.component';
import { UsuariosListComponent } from './components/usuarios/usuarios-list/usuarios-list.component';
import { UsuarioFormComponent } from './components/usuarios/usuario-form/usuario-form.component';
import { UsuarioDetailComponent } from './components/usuarios/usuario-detail/usuario-detail.component';
import { BarcosListComponent } from './components/barcos/barcos-list/barcos-list.component';
import { BarcoFormComponent } from './components/barcos/barco-form/barco-form.component';
import { BarcoDetailComponent } from './components/barcos/barco-detail/barco-detail.component';
import { ModelosListComponent } from './components/modelos/modelos-list/modelos-list.component';
import { ModeloFormComponent } from './components/modelos/modelo-form/modelo-form.component';
import { ModeloDetailComponent } from './components/modelos/modelo-detail/modelo-detail.component';
import { PartidasListComponent } from './components/partidas/partidas-list/partidas-list.component';
import { PartidaFormComponent } from './components/partidas/partida-form/partida-form.component';
import { PartidaDetailComponent } from './components/partidas/partida-detail/partida-detail.component';
import { GameBoardComponent } from './components/juego/game-board/game-board.component';
import { MapSelectorComponent } from './components/juego/map-selector/map-selector.component';
import { authGuard } from './guards/auth.guard';
import { adminGuard } from './guards/admin.guard';

export const routes: Routes = [
  // Rutas públicas
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  
  // Rutas autenticadas
  { path: 'home', component: HomeComponent, canActivate: [authGuard] },
  
  // Rutas de Usuarios (solo ADMIN)
  { path: 'usuarios', component: UsuariosListComponent, canActivate: [adminGuard] },
  { path: 'usuarios/new', component: UsuarioFormComponent, canActivate: [adminGuard] },
  { path: 'usuarios/:id', component: UsuarioDetailComponent, canActivate: [adminGuard] },
  { path: 'usuarios/:id/edit', component: UsuarioFormComponent, canActivate: [adminGuard] },
  
  // Rutas de Barcos (autenticadas - ADMIN + JUGADOR)
  { path: 'barcos', component: BarcosListComponent, canActivate: [authGuard] },
  { path: 'barcos/new', component: BarcoFormComponent, canActivate: [authGuard] },
  { path: 'barcos/:id', component: BarcoDetailComponent, canActivate: [authGuard] },
  { path: 'barcos/:id/edit', component: BarcoFormComponent, canActivate: [authGuard] },
  
  // Rutas de Modelos (autenticadas - ADMIN + JUGADOR)
  { path: 'modelos', component: ModelosListComponent, canActivate: [authGuard] },
  { path: 'modelos/new', component: ModeloFormComponent, canActivate: [authGuard] },
  { path: 'modelos/:id', component: ModeloDetailComponent, canActivate: [authGuard] },
  { path: 'modelos/:id/edit', component: ModeloFormComponent, canActivate: [authGuard] },
  
  // Rutas de Partidas (autenticadas - ADMIN + JUGADOR)
  { path: 'partidas', component: PartidasListComponent, canActivate: [authGuard] },
  { path: 'partidas/new', component: PartidaFormComponent, canActivate: [authGuard] },
  { path: 'partidas/:id', component: PartidaDetailComponent, canActivate: [authGuard] },
  
  // Rutas de Juego (autenticadas - ADMIN + JUGADOR)
  { path: 'game', component: MapSelectorComponent, canActivate: [authGuard] },
  { path: 'game/mapa/:mapaId', component: GameBoardComponent, canActivate: [authGuard] },
  { path: 'game/participacion/:participacionId', component: GameBoardComponent, canActivate: [authGuard] },
  
  // Ruta 404
  { path: '**', redirectTo: '/login' }
];
