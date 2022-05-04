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

# How it works?

Program ma kilka funkcji i możliwości, w założeniu ma wyciągać wszystkie dane z plików binarnych (i nie tylko), rozkodować je oraz zapisać w ustrukturyzowanej formie (CSV), którą można swobodnie edytować.

Twórca tłumaczenia może dzięki temu przetłumaczyć grę na dowolny język. Repozytorium nie udostępnia plików gry w celu ochrony praw autorskich, a jedynie podmienione fragmenty wraz z sumami kontrolnymi. (katalogi językowe)

Dla każdej "porcji danych" ma być wygenerowana suma kontrolna, która po utworzeniu tłumaczeń powinna potencjalnie umożliwić kompatybilność z każdą dostępną wersją gry wyszukując i podmieniając wyłącznie bity odpowiedzialne za przetłumaczone dialogi.

Pierwotna CSV zostaje na starcie okrojna z fragmentów będących "surowymi bajtami", niestety znaczna część bajtów musi zostać rozpoznana ręcznie. Niektóre zdekodowane fragmenty są rzeczywistym ciągiem znaków, które w języku angielskim mają znaczenie, ale nie są częścią dialogów gry. Z tego względu nie mogą być one modyfikowane, gdyż może to powodować błędy w działaniu gry.

# Links

Platformy do wspólnych tłumaczeń:

https://poeditor.com

https://weblate.org

Instalator Windows:

https://www.advancedinstaller.com
