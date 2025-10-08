import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModeloDetailComponent } from './modelo-detail.component';

describe('ModeloDetailComponent', () => {
  let component: ModeloDetailComponent;
  let fixture: ComponentFixture<ModeloDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ModeloDetailComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ModeloDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
