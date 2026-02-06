use std::fs;
use std::ops::RangeInclusive;

mod tests;

fn main() {
    println!("{}", detect_invalid_ids(
        get_content_as_string("rsc/sample"))
    );
}

pub fn detect_invalid_ids(content: String) -> u64 {
    to_ranges(content).iter()
        .map(|range| sum_invalid_ids_in(range.clone(), is_invalid_id))
        .sum()
}

pub fn detect_invalid_ids2(content: String) -> u64 {
    to_ranges(content).iter()
        .map(|range| sum_invalid_ids_in(range.clone(), is_invalid_id2))
        .sum()
}

fn sum_invalid_ids_in(range: RangeInclusive<u64>, invalid: impl Fn(u64) -> bool) -> u64 {
    range.filter(|&id| invalid(id))
        .sum()
}

fn is_invalid_id(id: u64) -> bool {
    let id_string = id.to_string();
    if id_string.len() % 2 == 0 {
        let split = id_string.split_at(id_string.len() / 2);
        return split.0 == split.1;
    }
    false
}

fn is_invalid_id2(id: u64) -> bool {
    let id_str = id.to_string();
    
    (1..=id_str.len() / 2)
        .any(|sub_len| {
            id_str.len() % sub_len == 0 &&
                (sub_len..id_str.len())
                    .step_by(sub_len)
                    .all(|i| &id_str[i..i + sub_len] == &id_str[0..sub_len])
        })
}

fn to_ranges(content: String) -> Vec<RangeInclusive<u64>> {
    content.split(",") //11-22
        .map(|c| to_range(c))
        .collect()
}

fn to_range(range_string: &str) -> RangeInclusive<u64> {
    let split: Vec<_> = range_string.split("-").collect();
    RangeInclusive::new(to_int(split[0]), to_int(split[1]))
}

fn to_int(content: &str) -> u64 {
    content.parse::<u64>().map_or(0, |i| i)
}

pub fn get_content_as_string(path: &str) -> String {
    fs::read_to_string(path)
        .unwrap()
}