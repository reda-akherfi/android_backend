

```powershell
docker run -d --name mongodb -p 27017:27017 `
  -e MONGO_INITDB_ROOT_USERNAME=admin `
  -e MONGO_INITDB_ROOT_PASSWORD=admin123 `
  -e MONGO_INITDB_DATABASE=documentDB `
  -v mongodb_data:/data/db `
  mongo:latest
```