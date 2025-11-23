<p align="center">
  <img src="./assets/banner.png" alt="Banner do Plugin Capivara" width="200"/>
</p>

<h1 align="center">
  Capivara - Análise de Fluxo de Controle para Eclipse
</h1>

<p align="center">
  Um plugin para a IDE Eclipse que analisa métodos Java para gerar Grafos de Fluxo de Controle e Tabelas Verdade, auxiliando na criação e visualização de casos de teste.
  <br />
  <br />
  <img alt="Versão" src="https://img.shields.io/badge/versão-1.0.0-blue">
  <img alt="Plataforma" src="https://img.shields.io/badge/plataforma-Eclipse-purple">
</p>

---

## Sumário


* [O que o Plugin Faz?](#o-que-o-plugin-faz)
* [Instalação](#instalação)
* [Como Usar](#como-usar)
* [Exemplo de Análise](#exemplo-de-análise)
* [Entendendo o Grafo: Legenda dos Nós](#entendendo-o-grafo-legenda-dos-nós)
* [Detalhes Técnicos](#detalhes-técnicos)


## O que o Plugin Faz? 

O **Capivara** é uma ferramenta de análise estática integrada à IDE Eclipse, projetada para auxiliar desenvolvedores e testadores a entender a lógica de seus métodos Java. Com um clique, ele gera três artefatos principais:

1.  **Enumeração de Nós:** Mapeia cada linha de código para seu nó correspondente no grafo.
2.  **Grafo de Fluxo de Controle:** Cria o GFC para ser vizualizado de forma integrada ao Eclipse IDE

## Instalação

### Método 1: Pelo Eclipse (Recomendado)

1.  No Eclipse, vá em `Help` > `Install New Software...`
2.  Clique em `Add...` e, no campo "Location", adicione a seguinte URL do nosso Update Site:
    > `https://eduardonascimentojf.github.io/capivara-plugin/`
3.  Selecione "Capivara" na lista, avance e conclua a instalação.

### Método 2: Instalação Manual (via Dropins)

1.  Baixe o arquivo `.jar` mais recente na nossa [página de Releases](https://github.com/eduardonascimentojf/capivara-plugin/releases).
2.  Copie o arquivo para a pasta `dropins` dentro do diretório de instalação do seu Eclipse e reinicie.


## Como Usar

O plugin adiciona novas opções ao menu de contexto do editor Java.

1.  No Eclipse, **selecione o código completo** de um método.
2.  Clique com o **botão direito** sobre a seleção.
3.  Vá até o menu **Capivara** e escolha a ação desejada:

| Opção | Descrição |
| :--- | :--- |
| **Node Enumerate** | Realiza apenas a **enumeração** das linhas do método selecionado. O plugin irá inserir comentários do tipo `/*Nó X*/` no seu código para facilitar a identificação, sem gerar gráficos. |
| **Generate GFC** | Cria o arquivo de modelo e abre automaticamente a visualização do **Grafo de Fluxo de Controle (GFC)**. <br>⚠️ *Nota: Esta ação cria um arquivo `capivara.gfc_diagram` no diretório do projeto, responsável pela renderização.* |

</br>



</br>





## Exemplo de Análise

**Código de Entrada:**

```java
public int exemplo(int a) {
    if (a > 0) {
        return a * 2;
    } else {
        return 0;
    }
}
```

**Resultados Gerados:**

<details>
<summary><strong>1. Enumeração de Nós</strong></summary>

```java
/*Linha 01*/ /*Nó 01*/ 		public int exemplo(int a) {
/*Linha 02*/ /*Nó 02*/ 		    if (a > 0) {
/*Linha 03*/ /*Nó 03*/ 		        return a * 2;
/*Linha 04*/ /*Nó 01*/ 		    } else {
/*Linha 05*/ /*Nó 04*/ 		        return 0;
/*Linha 06*/ /*Nó 01*/ 		    }
/*Linha 07*/ /*Nó 01*/ 		}

```

</details>



<details>
<summary><strong>2. Grafo </strong></summary>

![Grafo](./assets/capivara.png)

</details>

</br>



## Entendendo o Grafo: Legenda dos Nós

O grafo gerado utiliza cores e formas para diferenciar o papel de cada nó no fluxo de controle do seu método.

![Legenda dos Nós do Grafo](./assets/legenda.png)

| Tipo de Nó | Descrição |
| :--- | :--- |
| **ENTRY** (Verde) | O ponto de início do método. |
| **PROCESSING** (Preto) | Um ou mais comandos sequenciais. |
| **DECISION** (Azul) | Uma estrutura condicional, como `if`. |
| **LOOP DECISION** (Laranja)| Uma estrutura de laço, como `while` ou `for`. |
| **EXIT** (Vermelho, Círculo Duplo) | Um ponto de término do fluxo, como `return`. |



## Detalhes Técnicos

<<<<<<< HEAD
* **Versão Atual:** 1.1.0 
* **Plataforma Suportada:** Eclipse IDE 2021-03 ou superior
=======
* **Versão Atual:** 1.0.0 (Alpha)
* **Plataforma Suportada:** Eclipse IDE 2022-03 ou superior
* **Requisitos:** Java 17 ou superior

