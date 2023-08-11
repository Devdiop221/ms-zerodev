import dayjs from 'dayjs/esm';
import { ITag } from 'app/entities/tag/tag.model';

export interface IPost {
  id: number;
  title?: string | null;
  content?: string | null;
  date?: dayjs.Dayjs | null;
  tag?: Pick<ITag, 'id'> | null;
}

export type NewPost = Omit<IPost, 'id'> & { id: null };
