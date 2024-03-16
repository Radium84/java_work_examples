create function tool_generate_snils() returns text
    language plpgsql
as
$$
DECLARE
    orig_num BIGINT;
    control_sum BIGINT;
    digits INT[];
    sum INT;
    string_orig_num TEXT;
BEGIN
    orig_num := floor(random()*(999999999-100000000+1) + 100000000)::BIGINT; -- Generate number between 100000000 and 999999999
    digits := ARRAY(SELECT CAST(SUBSTRING(CAST(orig_num AS TEXT), n, 1) AS INT) -- Split number into array of digits
                    FROM generate_series(1, LENGTH(CAST(orig_num AS TEXT))) n);
    sum := 0;
    FOR i IN 1..LENGTH(CAST(orig_num AS TEXT)) LOOP
            sum := sum + (digits[i]*(10-i)); -- Multiply each digit by its position and sum up
        END LOOP;

    control_sum := sum%101; -- Get remainder of sum divided by 101
    IF control_sum = 100
        THEN control_sum:=0;
        END IF;
    string_orig_num := CAST(orig_num AS TEXT);
    string_orig_num = SUBSTRING(string_orig_num,1,3) || '-'|| SUBSTRING(string_orig_num,4,3)|| '-'||SUBSTRING(string_orig_num,7,3);
    RETURN string_orig_num || ' ' || control_sum::TEXT;
END;
$$;

alter function tool_generate_snils() owner to "KIS";