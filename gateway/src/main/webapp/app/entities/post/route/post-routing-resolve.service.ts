import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPost } from '../post.model';
import { PostService } from '../service/post.service';

@Injectable({ providedIn: 'root' })
export class PostRoutingResolveService implements Resolve<IPost | null> {
  constructor(protected service: PostService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPost | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((post: HttpResponse<IPost>) => {
          if (post.body) {
            return of(post.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
