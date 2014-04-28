Как запустить все это.

1. Забрать из гита.
2. В IDEA поставить Play 2.0 Support.
3. В идее сделать new play project и фолдером указать забранный из гита проект.
4. Поставить постгрес и создать какую нибудь базу {DATABASE_NAME}
5. Прописать (для osx & linux)

    sudo nano ~/.bash_profile

    export DATABASE_URL="jdbc:postgresql://localhost:5432/{DATABASE_NAME}?user={USER}&password={PASSWORD}"

    source ~/.bash_profile

    у меня подхватилось только после перезагрузки

6. Запустить из IDEA или через SBT