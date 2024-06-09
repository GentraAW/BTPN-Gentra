import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PagesItemComponent } from './pages-item.component';

describe('PagesItemComponent', () => {
  let component: PagesItemComponent;
  let fixture: ComponentFixture<PagesItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PagesItemComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PagesItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
