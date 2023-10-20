declare global {
  namespace NodeJS {
    interface ProcessEnv {
        [key: string]: string | undefined;
        DB_HOST: string;
        DB_PORT: string
        DB_LOGIN: string;
        DB_PASSWORD: string;
        DB_PATH: string;
        DB_CHARSET: string;
    }
  }
}
export {}