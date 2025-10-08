import { TestBed } from '@angular/core/testing';

import { ModelUtilsService } from './model-utils.service';

describe('ModelUtilsService', () => {
  let service: ModelUtilsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ModelUtilsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
