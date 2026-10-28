# Dados de exemplo do EasyGest

Este diretório contém 10 documentos JSON para cada coleção, exceto `usuarios`.
Os dados são fictícios. Os documentos relacionados usam IDs consistentes e
incluem os objetos embutidos esperados pelos modelos MongoDB.

## Importação no Windows PowerShell

Defina a URI no terminal sem colocá-la no repositório:

```powershell
$env:SPRING_MONGODB_URI = 'mongodb+srv://<usuario>:<senha>@<cluster>/EasyGest'
```

Com o MongoDB Database Tools instalado, execute os comandos na raiz do projeto
e importe uma vez, preferencialmente para um banco vazio:

```powershell
mongoimport --uri "$env:SPRING_MONGODB_URI" --db EasyGest --collection fornecedores --jsonArray --file database/mongodb-seed/fornecedores.json
mongoimport --uri "$env:SPRING_MONGODB_URI" --db EasyGest --collection clientes --jsonArray --file database/mongodb-seed/clientes.json
mongoimport --uri "$env:SPRING_MONGODB_URI" --db EasyGest --collection funcionarios --jsonArray --file database/mongodb-seed/funcionarios.json
mongoimport --uri "$env:SPRING_MONGODB_URI" --db EasyGest --collection prestadores --jsonArray --file database/mongodb-seed/prestadores.json
mongoimport --uri "$env:SPRING_MONGODB_URI" --db EasyGest --collection produtos --jsonArray --file database/mongodb-seed/produtos.json
mongoimport --uri "$env:SPRING_MONGODB_URI" --db EasyGest --collection compras --jsonArray --file database/mongodb-seed/compras.json
mongoimport --uri "$env:SPRING_MONGODB_URI" --db EasyGest --collection vendas --jsonArray --file database/mongodb-seed/vendas.json
mongoimport --uri "$env:SPRING_MONGODB_URI" --db EasyGest --collection itens_venda --jsonArray --file database/mongodb-seed/itens_venda.json
```

Os IDs são strings e os documentos usam `_id`, como espera o mapeamento
MongoDB dos modelos. As compras, vendas e itens de venda contêm cópias
embutidas dos fornecedores, clientes, produtos e vendas correspondentes.
O estoque e o total da venda já refletem os itens de exemplo.

O campo `usuario` dos funcionários fica `null`. Como os usuários serão
cadastrados depois, não envie esses funcionários ao endpoint da API antes de
criar os usuários correspondentes: o serviço de cadastro de funcionário exige
um usuário existente.

