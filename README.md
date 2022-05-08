# DeadBolt Translator Tool

# Original files taken into the translation:

- data.win
- deadbolt_game.exe
- dia_fp.json
- dia_lv1_4.json
- dia_lv2_1.json
- dia_lv2_3.json
- dia_lv3_5.json
- dia_lv3_7.json
- dia_lv4_2.json

# Commands

fetch - wyciąga ze wszystkich plików gry (wskazanych w kodzie na sztywno) dane, weryfikuje je pod kątem dialogów i odpowiednio umieszcza w jednolitej uporządkowanej strukturze, którą można poddać tłumaczeniu.

- input - ścieżka wejściowa, czyli katalog zawierający oryginalne pliki gry wymagane do przetłumaczenia.
- output - ścieżka wyjściowa, czyli tymczasowy dowolny katalog, który będzie przechowywać plik CSV przeznaczony do tłumaczenia. Docelowy CSV będzie uploadowany do POEditor Online, w którym wspólnie można dokonywać translacji gry na dowolny język.

patch - na podstawie gotowego pliku o dedykowanej strukturze (export z POEditor Online) oryginalne pliki gry zostaną zmodyfikowane. Proces ten obejmuje odczyt oryginalnych plików oraz ich modyfikację na podstawie danych zawartych w pliku CSV.

- input - ścieżka wejściowa, czyli CSV zawierający strukturę zawierającą wyeksportowane dialogi z gry wraz z tłumaczeniem
- output - ścieżka wyjściowa, czyli katalog z oryginalnymi plikami gry, które zostaną zmodyfikowane

# How it works?

Program ma kilka funkcji i możliwości, w założeniu ma wyciągać wszystkie dane z plików binarnych (i nie tylko), rozkodować je oraz zapisać w ustrukturyzowanej formie (CSV), którą można swobodnie edytować.

Twórca tłumaczenia może dzięki temu przetłumaczyć grę na dowolny język. Repozytorium nie udostępnia plików gry w celu ochrony praw autorskich, a jedynie podmienione fragmenty wraz z sumami kontrolnymi. (katalogi językowe)

Dla każdej "porcji danych" ma być wygenerowana suma kontrolna, która po utworzeniu tłumaczeń powinna potencjalnie umożliwić kompatybilność z każdą dostępną wersją gry wyszukując i podmieniając wyłącznie bity odpowiedzialne za przetłumaczone dialogi.

Pierwotna CSV zostaje na starcie okrojna z fragmentów będących "surowymi bajtami", niestety znaczna część bajtów musi zostać rozpoznana ręcznie. Niektóre zdekodowane fragmenty są rzeczywistym ciągiem znaków, które w języku angielskim mają znaczenie (prawdopodobnie to nazwy zmiennej lub obiektów), ale nie są częścią dialogów gry. Z tego względu nie mogą być one modyfikowane, gdyż może to powodować błędy w działaniu gry.

# Links

Platformy do wspólnych tłumaczeń:

https://alternativeto.net/software/po-editor/?license=free

https://poeditor.com (ograniczenie do 1k strings - string w tym wypadku to linijka oryginlana lub przetłumaczona)

https://weblate.org

Instalator Windows:

https://www.advancedinstaller.com
