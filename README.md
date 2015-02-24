# ghtools

Github tools

Configure by adding a src/ghtools/secrets.clj like so:

```
(ns ghtools.secrets)
(def slack-token "slack-token-goes-here")
(def channel-id "channel-id-to-post-to-goes-here")
(def repos ["user/repo1" "user/repo2"]) ; repos to get Pull requests from
(def gh-api "https://api.github.com") ; change to point to your enterprise API as needed
(def oauth-key "oauth-key-for-github")
```

Then run `lein run`

For development, run cider-jack-in from core_test.clj
then run (user/reset)
or
```
ghtools.core-test> (do (user/reset) (run-tests))
```

## Releases and Dependency Information


[Leiningen] dependency information:

    [ghtools "0.1.0-SNAPSHOT"]

[Maven] dependency information:

    <dependency>
      <groupId>ghtools</groupId>
      <artifactId>ghtools</artifactId>
      <version>0.1.0-SNAPSHOT</version>
    </dependency>

[Leiningen]: http://leiningen.org/
[Maven]: http://maven.apache.org/

## Usage

TODO



## Change Log

* Version 0.1.0-SNAPSHOT



## Copyright and License

Copyright © 2015 TODO_INSERT_NAME

TODO: [Choose a license](http://choosealicense.com/)
