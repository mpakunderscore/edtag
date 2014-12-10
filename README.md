How to run with IDEA.

1. git clone https://github.com/mpakunderscore/edtag.git
2. In IDEA install plugin Play 2.0 Support.
3. Create new Play project in IDEA and set folder of project
4. Install postgres and create local DB
5. Set {DATABASE_NAME} 

    sudo nano ~/.bash_profile
    export DATABASE_URL="jdbc:postgresql://localhost:5432/{DATABASE_NAME}?user={USER}&password={PASSWORD}"
    source ~/.bash_profile


6. Run project in IDEA or via SBT

How to run with Activator Framework

1. brew install activator
2. go to project folder
3. git clone https://github.com/mpakunderscore/edtag.git
3. brew install postgresql
4. run postgres DB
5. Set {DATABASE_NAME} 

    sudo nano ~/.bash_profile
    export DATABASE_URL="jdbc:postgresql://localhost:5432/{DATABASE_NAME}?user={USER}&password={PASSWORD}"
    source ~/.bash_profile

6. activator run

General advice
Use <a href="pgAdmin ">pgAdmin III</a> for DB Administration


Go into [File -> Project Structure -> Modules -> [YourAppModule] -> Sources Tab]
Navigate to the "target/scala-[version]/src_managed" directory (panel on the right).
Right click the "main" directory and flag it as a source folder. Right click "controllers" and "views" and remove the "sources" flag (you should see the controllers and views folder become package directories).
Apply -> OK -> recompile.