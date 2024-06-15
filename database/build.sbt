name := "democracy-vouchers-database"
description := "Libraries for database interfacing for the \"democracy vouchers\" system."
libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % Versions.slick,
)
scalacOptions ++= Seq(
  "-rewrite", "--source:3.4-migration"  // Deal with the old-style Slick code generation.
)
