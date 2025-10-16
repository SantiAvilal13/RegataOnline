import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BarcoFormComponent } from './barco-form.component';

describe('BarcoFormComponent', () => {
  let component: BarcoFormComponent;
  let fixture: ComponentFixture<BarcoFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BarcoFormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BarcoFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
