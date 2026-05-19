import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Capacity } from './capacity';

describe('Capacity', () => {
  let component: Capacity;
  let fixture: ComponentFixture<Capacity>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Capacity],
    }).compileComponents();

    fixture = TestBed.createComponent(Capacity);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
