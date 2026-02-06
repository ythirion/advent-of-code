#[cfg(test)]
mod tests {
    use crate::{detect_invalid_ids, detect_invalid_ids2, get_content_as_string};

    #[test]
    fn invalid_ids_for_sample() {
        assert_eq!(
            detect_invalid_ids(get_content_as_string("rsc/sample")),
            1227775554
        );
    }

    #[test]
    fn invalid_ids_for_sample2() {
        assert_eq!(
            detect_invalid_ids2(get_content_as_string("rsc/sample")),
            4174379265
        );
    }

    #[test]
    fn invalid_ids_for_part1() {
        assert_eq!(
            detect_invalid_ids(get_content_as_string("rsc/input")),
            5398419778
        );
    }

    #[test]
    fn invalid_ids_for_part2() {
        assert_eq!(
            detect_invalid_ids2(get_content_as_string("rsc/input")),
            15704845910
        );
    }
}