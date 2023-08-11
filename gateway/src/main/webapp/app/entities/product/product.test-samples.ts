import { IProduct, NewProduct } from './product.model';

export const sampleWithRequiredData: IProduct = {
  id: 77672,
  title: 'Customer-focused',
  price: 96307,
};

export const sampleWithPartialData: IProduct = {
  id: 34739,
  title: 'Unbranded deposit Bedfordshire',
  price: 54568,
  image: '../fake-data/blob/hipster.png',
  imageContentType: 'unknown',
};

export const sampleWithFullData: IProduct = {
  id: 7007,
  title: 'Administrator Health',
  price: 76905,
  image: '../fake-data/blob/hipster.png',
  imageContentType: 'unknown',
};

export const sampleWithNewData: NewProduct = {
  title: 'SAS Account',
  price: 42106,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
