import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BarcoDetailComponent } from './barco-detail.component';

describe('BarcoDetailComponent', () => {
  let component: BarcoDetailComponent;
  let fixture: ComponentFixture<BarcoDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BarcoDetailComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BarcoDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
