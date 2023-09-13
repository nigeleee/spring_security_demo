// import { Component } from '@angular/core';
// import { AuthenticationService } from '../service/authentication.service';
// import { Router } from '@angular/router';

// @Component({
//   selector: 'app-verifydetails',
//   templateUrl: './verifydetails.component.html',
//   styleUrls: ['./verifydetails.component.css']
// })
// export class VerifydetailsComponent {

//   constructor(private authService: AuthenticationService, private router: Router) {}

//   loading: boolean = true;

//   ngOnInit() {
//     this.authService.checkProfileComplete().subscribe({
//       next : (response:any) => {
//       this.loading = false;
//       if (response.profileComplete) {
//         this.router.navigate(['/products']);
//       } else {
//         this.router.navigate(['/complete-profile']);
//       }
//       }
//     });
//   }
// }
