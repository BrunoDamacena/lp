# SmallOO

## Sobre
Trabalho Prático 1 da Disciplina de Linguagens de Programação com o objetivo de desenvolver um interpretador para uma nova linguagem de programação que embute ideias de programação orientada a objetos: SmallOO

## Características da linguagem
### Tipos
- Inteiro (sequência de dígitos)
- String (sequência de caracteres entre aspas duplas)
- Objetos (armazenam propriedades que podem ser atributos ou funções)
- Funções (procedimentos que podem tomar opcionalmente qualquer quantidade de argumentos e retornar um valor)
- Lógicos (operações de comparações que obtém um valor lógico)

### Comandos
- if (executa comandos baseado em expressões condicionais)
- while (repete comandos enquanto a expressão condicional for verdadeira)
- atribuição (armazena um valor em uma variável)
- chamada (chama funções)

### Operações
- Matemáticas (adição, subtração, multiplicação, divisão e resto)
- Com String (concatenação)
- Lógicas (comparação, negação)
- Conectores (and, or)

Mais informações podem ser encontradas na [documentação do trabalho prático](documentation/TP1.pdf)

## Exemplo de código em SmallOO
```javascript
/* iniciar o array com nenhum elemento e tamanho 0. */
array.size = 0;

/* adicionar um novo elemento ao arranjo. */
array.add = function {
  system.set(self, ("element" + self.size), args.arg1);
  self.size = self.size + 1;
};

/* obter um elemento através de seu índice. */
array.get = function {
  return system.get(self, ("element" + args.arg1));
};

/* imprimir o arranjo. */
array.print = function {
  system.print("[ ");
  i = 0;
  while (i < self.size) {
    system.print(system.get(self, ("element" + i)) + " ");
    i = i + 1;
  }
  system.println("]");
};

/* entrar c/ numeros no arranjo. */
n = system.read("Entre c/ a qtd de numeros: ");
while (array.size < n) {
  x = system.random(0, 9);
  array.add(x);
}

/* criar uma copia do arranjo. */
array2 = system.clone(array);

/* adicionar mais um numero aleatorio a ele. */
y = system.random(0, 9);
array2.add(y);

/* imprimir o arranjo original. */
array.print();

/* mostrar qual elemento foi adicionado. */
system.println(y);

/* adicionar o método ordenar no novo arranjo utilizando o metodo bolha. */
array2.sort = function {
  a = 0;
  while (a < self.size) {
    b = a + 1;
    while (b < self.size) {
      x = system.get(self, ("element" + a));
      y = system.get(self, ("element" + b));
      if (y < x) {
        system.set(self, ("element" + a), y);
        system.set(self, ("element" + b), x);
      }
      b = b + 1;
    }
    a = a + 1;
  }
};

/* ordenar o novo arranjo e depois imprimir. */
array2.sort();
array2.print();
```

## Para compilar

### Utilizando o Terminal
1. Abra o terminal em src;
2. Compile utilizando `$ javac Main/sooi.java`
3. Execute utilizando `$ java Main.sooi examples/[nomeDoArquivo]`, sendo `nomeDoArquivo` o código em SmallOO, localizado na pasta [examples](project/SmallOO/src/examples).

### Utilizando o NetBeans
1. Vá em Executar > Definir Configuração do Projeto > Personalizar...
2. Em Argumentos, insira o código em SmallOO. Alguns exemplos podem ser encontrados em `src/examples/[nomeDoArquivo]`.
