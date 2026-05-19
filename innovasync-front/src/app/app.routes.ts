import { Routes } from '@angular/router';
import { Capacity } from './features/capacity/capacity';
import { Projects} from './features/projects/projects';
import { Dashboard } from './features/dashboard/dashboard';
import { Login } from './features/login/login';
import { Configuration } from './features/configuration/configuration';

export const routes: Routes = [
    {
        path: 'login',
        component: Login
    },
    {
        path: 'dashboard',
        component: Dashboard
    },
    {
        path: 'projects',
        component: Projects
    },
    {
        path: 'capacity',
        component: Capacity
    },
    {
        path: 'configuration',
        component:Configuration
    },
    {
        path:'',
        redirectTo: 'login',
        pathMatch: 'full'

    }
];
