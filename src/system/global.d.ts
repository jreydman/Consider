export {};
declare global {
  namespace NodeJS {
    interface ProcessEnv {
      APP_name: string;
      Prefix: string;
    }
  }
}
