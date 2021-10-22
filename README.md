# Simple Query Editor

Simple query editor witch used in some project

Due to all queries were store in the database, and for reason original develop tools are missing, so I have to create my own tools for edit those queries.

It will not that useful in most case, but I decided to leave it just for record.

## Installation

1. Download jdbc driver from desired database provider and put it in `/libs`
2. Copy `/src/mybatis-config-sample.xml` to `/src/mybatis-config.xml` and edit database information
3. Goto `/src/me/litz/util/SessionFactoryUtils.java` and alter `private static String database = "EMS";` in line 13 to any database property id you previously declared in `/src/mybatis-config.xml`.
4. Goto `/src/me/litz/window/SideBar.java` to edit `database = new JComboBox<>(new String[]{"EMS", "ESS"});` to all database property id you previously declared in `/src/mybatis-config.xml`.
5. Feel free to execute `/src/me/litz/Application.java`.

## Notice

This project was use Altibase for its database.