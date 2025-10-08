import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './components/home/home/home.component';
import { UsuariosListComponent } from './components/usuarios/usuarios-list/usuarios-list.component';
import { UsuarioFormComponent } from './components/usuarios/usuario-form/usuario-form.component';
import { BarcosListComponent } from './components/barcos/barcos-list/barcos-list.component';
import { BarcoFormComponent } from './components/barcos/barco-form/barco-form.component';
import { ModelosListComponent } from './components/modelos/modelos-list/modelos-list.component';
import { ModeloFormComponent } from './components/modelos/modelo-form/modelo-form.component';
import { PartidasListComponent } from './components/partidas/partidas-list/partidas-list.component';
import { PartidaFormComponent } from './components/partidas/partida-form/partida-form.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    CommonModule,
    HomeComponent,
    UsuariosListComponent,
    UsuarioFormComponent,
    BarcosListComponent,
    BarcoFormComponent,
    ModelosListComponent,
    ModeloFormComponent,
    PartidasListComponent,
    PartidaFormComponent
  ],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = 'Regata Online';
  
  currentSection = signal<string>('home');
  currentSubSection = signal<string>('list');

  showSection(section: string) {
    this.currentSection.set(section);
    this.currentSubSection.set('list'); // Reset to list view
  }

  showSubSection(section: string, subSection: string) {
    this.currentSection.set(section);
    this.currentSubSection.set(subSection);
  }

  onSectionChange(event: {section: string, subSection?: string}) {
    this.currentSection.set(event.section);
    if (event.subSection) {
      this.currentSubSection.set(event.subSection);
    }
  }
}
