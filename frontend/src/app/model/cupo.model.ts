
export interface ParejaCupo {
  id: number;
  nombre: string;
  documento: string;
  cupoAsignado: number;
  restriccionActiva: boolean;
}

export interface TitularCupo {
  id: number;
  nombre: string;
  correo: string;
  fechaRegistro: string;
  estado: 'Activo' ;
  cupoTotal: number;
  parejas: ParejaCupo[];
}


