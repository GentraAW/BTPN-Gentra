import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PagesOrderComponent } from './pages-order.component';

describe('PagesOrderComponent', () => {
  let component: PagesOrderComponent;
  let fixture: ComponentFixture<PagesOrderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PagesOrderComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PagesOrderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
