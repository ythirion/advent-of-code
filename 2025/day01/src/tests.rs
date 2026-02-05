#[cfg(test)]
mod tests {
    use crate::{find_password, find_password2};
    use std::fs;

    #[test]
    fn password_for_sample_part1() {
        assert_eq!(
            find_password(get_content_as_string("rsc/sample")),
            3
        );
    }

    #[test]
    fn password_for_sample_part2() {
        assert_eq!(
            find_password2(get_content_as_string("rsc/sample")),
            6
        );
    }

    #[test]
    fn password_for_part1() {
        assert_eq!(
            find_password(get_content_as_string("rsc/input")),
            1177
        );
    }

    #[test]
    fn password_for_part2() {
        assert_eq!(
            find_password2(get_content_as_string("rsc/input")),
            6768
        );
    }

    fn get_content_as_string(path: &str) -> Vec<String> {
        fs::read_to_string(path)
            .map(|content| content.lines().map(String::from).collect())
            .unwrap()
    }
}