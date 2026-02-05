mod tests;

const START_AT: i32 = 50;

fn main() {
    println!("Hello, world!");
}

fn find_password(instructions: Vec<String>) -> i32 {
    let mut current_password = START_AT;
    instructions
        .iter()
        .map(|instruction| {
            current_password = new_password(instruction, current_password);
            current_password
        })
        .filter(|password| *password == 0)
        .count() as i32
}

fn delta_for(instruction: &String) -> i32 {
    if instruction.starts_with("L") {
        -to_int(instruction)
    } else {
        to_int(instruction)
    }
}

fn to_int(instruction: &str) -> i32 {
    instruction[1..]
        .parse::<i32>()
        .unwrap()
}

fn new_password(instruction: &String, current_password: i32) -> i32 {
    (current_password + delta_for(instruction)) % 100
}

fn new_password2(instruction: &str, current: i32) -> (i32, i32) {
    let steps = to_int(instruction);
    let delta = if instruction.starts_with("L") {
        -steps
    } else {
        steps
    };

    let hits = zero_hits(current, delta);
    let new_pos = (current + delta).rem_euclid(100);

    (new_pos, hits)
}

fn zero_hits(current: i32, delta: i32) -> i32 {
    if delta == 0 { return 0; }
    let steps = delta.abs();
    let mut first = if delta > 0 { (100 - current).rem_euclid(100) } else { current.rem_euclid(100) };

    if first == 0 { first = 100; }
    if first > steps { 0 } else { 1 + (steps - first) / 100 }
}


fn find_password2(instructions: Vec<String>) -> i32 {
    let mut current = START_AT;
    let mut count = 0;

    for instruction in instructions {
        let (new_pos, hits) = new_password2(&instruction, current);
        current = new_pos;
        count += hits;
    }

    count
}
