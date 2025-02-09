import { atomWithStorage } from 'jotai/utils'
import { atom } from 'jotai';

export const jwtAtom = atomWithStorage('jwtAtom', { access: '' })

export const expAtom = atomWithStorage('expAtom', 0)

export const accessTokenAtom = atom(null);