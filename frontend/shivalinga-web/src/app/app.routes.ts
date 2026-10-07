import { Routes } from '@angular/router';
import { HomePage } from './web/home-page';
import { WebOurStory } from './web/web-our-story/web-our-story';

export const routes: Routes = [
  { path: '', component: HomePage, title: 'Shivalinga Kala Kendra | Where Tradition Meets Expression' },
  { path: 'our-story', component: WebOurStory, title: 'Our Story | Shivalinga Kala Kendra' },
  { path: '**', redirectTo: '' },
];
