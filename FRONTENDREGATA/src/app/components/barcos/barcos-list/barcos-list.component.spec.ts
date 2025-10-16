import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BarcosListComponent } from './barcos-list.component';

describe('BarcosListComponent', () => {
  let component: BarcosListComponent;
  let fixture: ComponentFixture<BarcosListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BarcosListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BarcosListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
