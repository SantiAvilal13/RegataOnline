import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
  @Output() sectionChange = new EventEmitter<{section: string, subSection?: string}>();

  navigateToSection(section: string) {
    this.sectionChange.emit({ section, subSection: 'list' });
  }

  navigateToForm(section: string) {
    this.sectionChange.emit({ section, subSection: 'form' });
  }
}
