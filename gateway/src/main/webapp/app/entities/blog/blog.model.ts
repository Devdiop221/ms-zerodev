import { IPost } from 'app/entities/post/post.model';

export interface IBlog {
  id: number;
  name?: string | null;
  handle?: string | null;
  post?: Pick<IPost, 'id'> | null;
}

export type NewBlog = Omit<IBlog, 'id'> & { id: null };
