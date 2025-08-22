import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { authBlockerGuard } from './auth-blocker-guard';

describe('authBlockerGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => authBlockerGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
