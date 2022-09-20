Autores: Matheus Pozzer Moraes, Caroline Lewandowski Rodrigues, Camila Borba Rocha.

Para rodar o código basta colocar algum arquivo txt, na pasta fonte do projeto(src), e setar na classe App.java
o arquivo a ser lido, como por exemplo: FileReader arq = new FileReader(<arquivo.txt>);

O arquivo txt tem como estrutura G/G/1/5,2..4,3..5, onde as quatros primeiras variáveis são separadas por barra(/), 
onde a primeira e a segunda indicam o tipo de distribuição, a terceira a quantidade de servidores e a quarta a capacidade
da fila. A quinta e sexta variáveis são separadas por virgula(,), na quarta variável é indicado o intervalo de tempo
de chegada de clientes, onde o intervalo é separado por dois pontos(..), a sexta variável indica o tempo para ser realizado
um serviço.

O número de repetições está sendo colocado hardcoded, no caso 5, pois foi pedido somente 5 repetições.

Os 2 casos de testes pedidos na primeira parte do T1 já estão na pasta fonte do projeto, onde a fila 1 é o primeiro caso
e a fila 2 é o segundo.