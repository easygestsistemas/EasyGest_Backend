# Dados de exemplo do EasyGest

Este diretório contém 10 documentos JSON para cada coleção, exceto `usuarios`.
Os dados são fictícios. Os documentos relacionados usam IDs consistentes e
incluem os objetos embutidos esperados pelos modelos MongoDB.

## Importação no Windows PowerShell

Defina a URI no terminal sem colocá-la no repositório:

```powershell
$env:SPRING_MONGODB_URI = 'mongodb+srv://<usuario>:<senha>@<cluster>/EasyGest'
```

Com o MongoDB Database Tools instalado, execute na raiz do projeto:

```powershell
.\database\mongodb-seed\import-mongodb.ps1
```

O script lê `SPRING_MONGODB_URI` do `.env.local` ou do ambiente, importa fornecedores,
clientes, funcionários, prestadores e produtos e não apaga coleções existentes.

Para substituir completamente esses dados de demonstração, informe `-Drop`:

```powershell
.\database\mongodb-seed\import-mongodb.ps1 -Drop
```

Esta opção apaga somente as cinco coleções importadas antes de inserir os arquivos JSON.
Não use `-Drop` se houver dados reais nessas coleções.

Antes da importação, no MongoDB Atlas configure:

1. Database Access: usuário com permissão para ler e gravar no banco `EasyGest`.
2. Network Access: IP público `0.0.0.0/0` para desenvolvimento ou o IP da máquina para acesso restrito.
3. A URI com o formato `mongodb+srv://USUARIO:SENHA@CLUSTER/EasyGest`.

Se o usuário ou senha tiver caracteres especiais, codifique-os na URI. Não informe o valor real em mensagens ou arquivos versionados.

Os IDs são strings e os documentos usam `_id`, como espera o mapeamento
MongoDB dos modelos. Registros financeiros reais devem ser cadastrados pela
API ou inseridos manualmente, sem utilizar dados de demonstração do repositório.

O campo `usuario` dos funcionários fica `null`. Como os usuários serão
cadastrados depois, não envie esses funcionários ao endpoint da API antes de
criar os usuários correspondentes: o serviço de cadastro de funcionário exige
um usuário existente.

