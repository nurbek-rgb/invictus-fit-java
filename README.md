# invictus-fit-java

# Invictus Fitness: абонементтер, тренерлер, қызметтер

Java жобасы: **«Фитнес клуб абонементтері»**

Деректер invictus.kz сайтындағы клуб беттерінен алынды (28.10.2026). Сайтта көрсетілмеген нәрсе «табылмады» деп жазылған.

## 1. Жоба кластары

| Класс | Рөлі |
|---|---|
| `Member` | Клуб мүшесі |
| `Membership` (abstract) | Абонементтің базалық класы |
| `Trainer` | Жаттықтырушы |
| `GroupClass` | Топтық сабақ |
| `Visit` | Клубқа келу фактісі |
| `Schedule` | Сабақ кестесі |

## 2. Клубтар: баға, тренер, сабақ

Бағалар — жылдық абонемент (сайтта әр клубқа бір жылдық баға көрсетілген).

| Клуб | Жылдық баға | Тренер (сайтта)* | Топтық сабақ | Қызметтер / ерекшелігі |
|---|---|---|---|---|
| Fitness Sadu (Әл-Фараби 128/7) | сайтта жоқ | 99+ | 43 | 7000 м², жаттығу залы 2300 м², 7 жолақты бассейн, паркинг, InBody |
| Fitness Green Mall (Сыганак 17П) | 770 000 ₸ | 99+ | 24 | 25 м бассейн, желідегі ең үлкен жаттығу залы |
| Fitness Highvill (Байтурсынов 9) | 654 500 ₸ | 96 | 20 | Бассейн |
| Fitness Gagarin (Алматы, Гагарин 286) | 260 000 ₸ | 46 | 21 | Сынақ: 3 жаттығу — 4 990 ₸ |
| GO OXY (Астана, Сыгынак 6п) | 219 000 ₸ | 57 | 18 | 4000 м², Technogym және Hoist |
| GO Arena | 219 000 ₸ | 26 | 11 | 2000 м², 80+ тренажер, 3 топтық зал, 2 раздевалкада хамам |
| GO Gres (Жансүгіров 159) | 189 000 ₸ | 25 | 18 | 1500 м², Technogym |

\* Сайттағы тренер саны сенімсіз: бір филиалда нақтысында шамамен 50-ге дейін тренер бар, ал сайт (мысалы, «99+») дұрыс көрсетпейді. Жобада бір клубқа 15–50 тренер алынады.

## 3. Абонемент түрлері

- **Жылдық абонемент** — негізгі өнім, бағасы клубқа байланысты: 189 000 ₸ (GO Gres) – 770 000 ₸ (Green Mall).
- **Сынақ пакеті (Trial)** — 3 жаттығу: GO клубтарында 2 990 ₸, Gagarin клубында 4 990 ₸.
- **Бөліп төлеу** — Invictus GO үшін ай сайынғы төлем бар. Finratings.kz (наурыз 2025) бойынша бастапқы жарна 17 000 → 20 000 ₸, айлық төлем 19 000 → 28 000 ₸. Қазіргі баға басқаша болуы мүмкін.

**Табылмады:** 1 / 3 / 6 ай абонементтері, мұздату (freeze), жеке жаттығу бағалары клуб беттерінде жоқ. Жобада `MonthlyMembership` өзіміз қосқан модель.

## 4. Қызметтер

| Қызмет | Қай клубта |
|---|---|
| Бассейн | Sadu (7 жолақ), Green Mall (25 м), Highvill |
| Хамам | Invictus сайтында бар (жаттығудан кейін демалу үшін); GO Arena-да 2 раздевалкада |
| Икемділік аймағы | Созылуға арналған аймақ |
| Топтық жаттығулар | 25-тен астам түрі: сайклинг, йога, стретчинг, буткемп, бачата, пилатес және басқалар |
| Сауна | Sadu — тек 2ГИС тізімінде, клуб сайтында жоқ (нақтылау керек) |
| InBody, паркинг | Sadu |
| Балалар сабақтары | Healthy Back Junior (9–15 жас) — Sadu |
| Жұмыс уақыты | Дс–Жм 07:00–23:00/00:00, Сб–Жс 09:00–22:00/23:00 |

**Топтық сабақ мысалдары:** Bachata, Zumba, Invictus Dance, Invictus Kick, Invictus Strong, Cycle, HIIT, Cross Training, Pilates mat, Yoga, Kundalini Yoga, Stretching, МФР, Aqua Prenatal.

## 5. Java модельі

| Класс | Өрістер | Деректер қайдан |
|---|---|---|
| `Membership` (abstract) | id, startDate, price, club; `getDurationDays()`, `calculatePrice()` | Жылдық баға клубқа тәуелді |
| `AnnualMembership` | — | 189 000 – 770 000 ₸ |
| `TrialMembership` | visitsLeft = 3 | 2 990 / 4 990 ₸ |
| `MonthlyMembership` | 30 күн | Жоба үшін (сайтта жоқ) |
| `Member` | id, fullName, phone, membership, visits | — |
| `Trainer` | id, fullName, specialization, clubs | Клубта 15–50 тренер |
| `GroupClass` | name, category, level, capacity, trainer | Клубта 11–43 сабақ |
| `Visit` | member, dateTime, groupClass | — |
| `Schedule` | `Map<DayOfWeek, List<GroupClass>>` | Кесте |
| `Club` (қосымша) | name, address, hasPool, hasHammam | 2-кестедегі клубтар |

## 6. Қалай бастау керек

1. **Құралдар:** JDK 21+, IntelliJ IDEA, Maven, JUnit 5. Алдымен консольдық нұсқа.
2. **Enum-дар:** `ClassCategory` (CARDIO, MIND_BODY, STRENGTH, FUNCTIONAL, DANCE), `Level` (EASY, MEDIUM).
3. **Класстар:** `Membership` (abstract) → `AnnualMembership`, `TrialMembership`, `MonthlyMembership`; сосын `Member`, `Trainer`, `GroupClass`, `Visit`, `Schedule`.
4. **Тест деректері:** 7 клуб, 3–4 тренер, 8–10 сабақ.
5. **Ережелер:** мерзімі өткен абонементпен кіргізбеу; сабақ толса `ClassFullException`; Trial-да `visitsLeft` азайту.
6. **Консольдық меню:** мүше тіркеу, абонемент сатып алу, сабаққа жазылу, кіруді тіркеу, кестені көру.
7. **Тест және сақтау:** JUnit тесттері, кейін CSV/JSON файл немесе SQLite.


## Дереккөздер

- [invictus.kz — Sadu](https://invictus.kz/ru-kz/clubs/fitness/fitness-sadu)
- [invictus.kz — Green Mall](https://invictus.kz/ru-kz/clubs/go/fitness-green-mall)
- [invictus.kz — Highvill](https://invictus.kz/ru-kz/clubs/fitness/fitness-highvill)
- [invictus.kz — Gagarin](https://invictus.kz/ru-kz/clubs/girls/fitness-gagarin)
- [invictus.kz — GO OXY](https://invictus.kz/ru-kz/clubs/go/go-oxy)
- [invictus.kz — GO Arena](https://invictus.kz/ru-kz/clubs/go/go-arena)
- [invictus.kz — GO Gres](https://invictus.kz/ru-kz/clubs/go/go-gres)
- [Finratings.kz — Invictus Go бағасы өсті (25.03.2025)](https://finratings.kz/news/1671-tseny-vyrosli-obiasnenii-net-klienty-invictus-go-vozmushcheny-podorozhaniem/)
- [2ГИС — Invictus Fitness Sadu](https://2gis.kz/almaty/firm/70000001057033854)
