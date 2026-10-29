# SC2002 Restaurant Rush

Repository: [SC2002_SCEB_Grp2](https://github.com/jaedonkhoo/SC2002_SCEB_Grp2)

## Build and run

Requires JDK 11 or later. Run from the repository root in a macOS or Linux terminal:

```sh
mkdir -p out
javac --release 11 -d out src/*.java
java -cp out Main
```

## Customer food selection

Customers choose food using `(customerId - 1) % 3`, with the menu ordered Salad,
Burger, Pasta. IDs follow arrival order: customers 1 and 4 choose Salad, 2 and 5
choose Burger, and 3 and 6 choose Pasta.

## Stage 1 test notes

See [TESTING.md](TESTING.md) for reproducible victory and defeat runs.

## Stage comparisons

Pending Stage 2 and Stage 3 implementation. For each transition, the comparison
will list new files, changed or deleted files with reasons, and the relevant
commit or compare link.

## Video timestamps

Pending recording.
