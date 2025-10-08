import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Partida } from '../../../models';
import { PartidaService } from '../../../shared/services/partidas/partida.service';

@Component({
  selector: 'app-partida-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './partida-form.component.html',
  styleUrl: './partida-form.component.css'
})
export class PartidaFormComponent implements OnInit {
  partida = signal<Partida>(new Partida());
  saving = signal(false);
  error = signal<string | null>(null);
  
  partidaService = inject(PartidaService);

  ngOnInit() {
    // Component initialization
  }

  savePartida() {
    if (!this.partida().isValid()) {
      this.error.set('Por favor completa todos los campos requeridos');
      return;
    }

    this.saving.set(true);
    this.error.set(null);

    const partida = this.partida();
    
    this.partidaService.createPartida(partida).subscribe({
      next: () => {
        this.saving.set(false);
        this.partida.set(new Partida()); // Reset form
        alert('Partida creada exitosamente');
      },
      error: (error) => {
        this.error.set('Error al crear partida: ' + error.message);
        this.saving.set(false);
      }
    });
  }
}
