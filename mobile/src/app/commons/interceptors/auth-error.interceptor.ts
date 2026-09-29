import { HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable } from "@angular/core";

@Injectable()
export class HttpAuthErrorInterceptor implements HttpInterceptor {
	constructor(
		// private authService: AuthService
		) {}

	intercept(request: HttpRequest<any>, next: HttpHandler) {
		// return next.handle(request).pipe(
		//   catchError(err => {
		//     if (err.status === 401) {
		//       this.authService.logout();
		//       location.reload();
		//     }

		//     // return throwError(err.error.message || err.statusText);
		//     throw err;
		//   }),
		// );
		return next.handle(request);
	}
}
