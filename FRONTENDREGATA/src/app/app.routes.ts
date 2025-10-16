import { Routes } from '@angular/router';
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



export const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent },
  
  // Rutas de Usuarios
  { path: 'usuarios', component: UsuariosListComponent },
  { path: 'usuarios/new', component: UsuarioFormComponent },
  { path: 'usuarios/:id', component: UsuarioDetailComponent },
  { path: 'usuarios/:id/edit', component: UsuarioFormComponent },
  
  // Rutas de Barcos
  { path: 'barcos', component: BarcosListComponent },
  { path: 'barcos/new', component: BarcoFormComponent },
  { path: 'barcos/:id', component: BarcoDetailComponent },
  { path: 'barcos/:id/edit', component: BarcoFormComponent },
  
  // Rutas de Modelos
  { path: 'modelos', component: ModelosListComponent },
  { path: 'modelos/new', component: ModeloFormComponent },
  { path: 'modelos/:id', component: ModeloDetailComponent },
  { path: 'modelos/:id/edit', component: ModeloFormComponent },
  
  // Rutas de Partidas
  { path: 'partidas', component: PartidasListComponent },
  { path: 'partidas/new', component: PartidaFormComponent },
  { path: 'partidas/:id', component: PartidaDetailComponent },
  
      // Rutas de Juego
      { path: 'game', component: MapSelectorComponent },
      { path: 'game/mapa/:mapaId', component: GameBoardComponent },
      { path: 'game/participacion/:participacionId', component: GameBoardComponent },
  
  // Ruta 404
  { path: '**', redirectTo: '/home' }
];
