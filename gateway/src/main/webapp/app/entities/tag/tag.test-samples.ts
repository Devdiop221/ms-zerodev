import { ITag, NewTag } from './tag.model';

export const sampleWithRequiredData: ITag = {
  id: 42372,
  name: 'human-resource',
};

export const sampleWithPartialData: ITag = {
  id: 72152,
  name: 'Officer Sleek Extended',
};

export const sampleWithFullData: ITag = {
  id: 82371,
  name: 'Mongolia',
};

export const sampleWithNewData: NewTag = {
  name: 'heuristic next-generation',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
