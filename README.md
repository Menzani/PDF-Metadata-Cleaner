Overwrites the specified PDF file with a cleaner version with no metadata.

Requires at least Java 11.  
Get the [fat JAR](https://github.com/Menzani/PDF-Metadata-Cleaner/releases/download/v1.0/PDF_Metadata_Cleaner.jar).

Usage: `java -jar PDF_Metadata_Cleaner.jar`  
Recommended VM options: `-Xms16m -Xmx16m -XX:-UsePerfData -Xint -XX:+UnlockExperimentalVMOptions -XX:+UseEpsilonGC`

Exit codes:
- **1** ⟶ The PDF file is already clean.
- **2** ⟶ Could not read or parse the PDF file.
- **3** ⟶ The PDF document requires a password or the password provided is incorrect.
- **4** ⟶ Could not overwrite the PDF file.
- **5** ⟶ Too few program arguments.