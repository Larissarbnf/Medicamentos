# 💊 Minhas Pílulas

Minhas Pílulas é um aplicativo Android moderno para gerenciamento de medicamentos, desenvolvido com Kotlin e Jetpack Compose. O app oferece persistência local de dados, atualização reativa da interface e salvamento da preferência de tema (claro/escuro) utilizando tecnologias atuais do ecossistema Android.

## 📱 Sobre o Projeto

O Minhas Pílulas foi criado para auxiliar usuários no controle e organização de seus medicamentos de forma simples e eficiente.

O aplicativo permite cadastrar medicamentos contendo:
- Nome
- Horário de uso
- Frequência
- Observações adicionais

Todos os dados são armazenados localmente no dispositivo por meio do **Room Database**, garantindo funcionamento offline e privacidade do usuário.

Além disso, o aplicativo possui suporte a tema claro e escuro, cuja preferência é persistida com **DataStore Preferences**, assegurando que a configuração escolhida seja mantida entre as execuções do app.

## ✨ Funcionalidades

- ➕ Adicionar medicamentos com informações detalhadas
- ✏️ Editar medicamentos existentes
- 🗑️ Excluir medicamentos
- 📋 Visualizar lista completa de medicamentos
- 🔄 Atualização automática da interface em tempo real
- 🌗 Alternância entre tema claro e escuro
- 💾 Persistência local de dados com Room
- 🎨 Persistência da preferência de tema com DataStore Preferences

## 🛠️ Tecnologias Utilizadas

- Kotlin
- Jetpack Compose
- Room Database (SQLite)
- Coroutines
- Flow
- DataStore Preferences
- Material Design 3

## 🏗️ Arquitetura do Projeto

O projeto segue boas práticas de organização e separação de responsabilidades.
```
app/
├── data/
│   ├── entities/
│   │   └── MedicamentoEntity.kt      # Entidade do banco de dados
│   ├── dao/
│   │   └── MedicamentoDao.kt         # Operações CRUD
│   ├── db/
│   │   ├── AppDatabase.kt            # Configuração do Room
│   │   └── DatabaseProvider.kt       # Singleton do banco
│   └── datastore/
│       └── SettingsDataStore.kt      # Preferências do usuário (Tema)
└── MainActivity.kt                    # Interface principal (Compose)
```

## 📐 Padrões e Conceitos Utilizados

**Repository Pattern**  
Centraliza o acesso aos dados, facilitando manutenção e testes.

**Singleton Pattern**  
Garante uma única instância do banco de dados Room.

**MVVM (implícito)**  
Separação entre interface do usuário e lógica de negócios.

**Programação Reativa**  
Uso de Flow para atualização automática da interface.

## 🎨 Tema e Preferências do Usuário

A aplicação permite alternar entre modo claro e modo escuro diretamente pela interface.

A preferência do usuário é salva utilizando **DataStore Preferences**, garantindo que:
- O tema escolhido seja restaurado automaticamente ao reabrir o app
- A persistência seja feita de forma assíncrona e segura
- A aplicação utilize uma API moderna baseada em Flow, substituindo o uso de SharedPreferences

## 🔄 Fluxo de Dados
```
Usuário → Interface → Validação → Coroutine → Room Database
                                                      ↓
                                                    Flow
                                                      ↓
                                     Atualização automática da UI
```

## 🚀 Como Executar o Projeto

1. Clone o repositório:
```bash
git clone https://github.com/Larissarbnf/Medicamentos.git
```

2. Abra o projeto no Android Studio

3. Sincronize o Gradle

4. Execute em um emulador ou dispositivo físico

## 📋 Requisitos

- Android Studio Arctic Fox ou superior
- SDK mínimo: Android 7.0 (API 24)
- SDK alvo: Android 14 (API 34)

## 🐛 Solução de Problemas

### ❌ Medicamentos não aparecem após salvar

- Verifique se o Flow está sendo coletado corretamente
- Confirme o uso adequado de Coroutines
- Certifique-se de que o estado da UI está sendo atualizado

### ❌ Tema não persiste após fechar o app

- Verifique a leitura e gravação do DataStore Preferences
- Confirme a coleta do Flow de tema dentro do setContent

## 📌 Considerações Finais

Este projeto demonstra o uso de boas práticas modernas no desenvolvimento Android, com foco em:
- Arquitetura limpa
- Programação reativa
- Persistência eficiente
- Interface moderna com Jetpack Compose
