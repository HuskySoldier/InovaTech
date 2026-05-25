import { Routes } from '@angular/router';
import { Capacity } from './features/capacity/capacity';
import { Projects } from './features/projects/projects';
import { Dashboard } from './features/dashboard/dashboard';
import { Login } from './features/login/login';
import { Configuration } from './features/configuration/configuration';
// 1. Importa el guard que creamos
import { authGuard } from './core/guards/auth.guard'; 

export const routes: Routes = [
    {
        path: 'login',
        component: Login
    },
    // Rutas protegidas: solo accesibles si el guard devuelve 'true'
    {
        path: 'dashboard',
        component: Dashboard,
        canActivate: [authGuard] 
    },
    {
        path: 'projects',
        component: Projects,
        canActivate: [authGuard]
    },
    {
        path: 'capacity',
        component: Capacity,
        canActivate: [authGuard]
    },
    {
        path: 'configuration',
        component: Configuration,
        canActivate: [authGuard]
    },
    {
        path: '',
        redirectTo: 'login',
        pathMatch: 'full'
    }
];