import Hashids from 'hashids';

const hashids = new Hashids('cinephile_app_salt', 8);

export const encodeId = (id) => hashids.encode(id);
export const decodeId = (id) => hashids.decode(id);
