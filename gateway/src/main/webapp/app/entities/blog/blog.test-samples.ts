import { IBlog, NewBlog } from './blog.model';

export const sampleWithRequiredData: IBlog = {
  id: 53708,
  name: 'THX compressing object-oriented',
  handle: 'base Balboa',
};

export const sampleWithPartialData: IBlog = {
  id: 82182,
  name: 'Rustic',
  handle: 'blue and Borders',
};

export const sampleWithFullData: IBlog = {
  id: 6686,
  name: 'Utah Shirt end-to-end',
  handle: 'payment Account',
};

export const sampleWithNewData: NewBlog = {
  name: 'Centralized bluetooth',
  handle: 'blue withdrawal Cambridgeshire',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
